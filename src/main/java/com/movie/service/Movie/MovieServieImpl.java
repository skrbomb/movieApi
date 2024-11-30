package com.movie.service.Movie;


import com.movie.dto.MovieDto;
import com.movie.dto.MoviePageResponse;
import com.movie.entities.Movie;
import com.movie.exceptions.EmptyFileException;
import com.movie.exceptions.FileExistsException;
import com.movie.exceptions.MovieNotFoundException;
import com.movie.repositories.MovieRepository;
import com.movie.service.File.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServieImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        //1.upload the file

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! Please enter another filename");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        //2.set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);

        //3.map dto to Movie object
        Movie movie=new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //4.save the movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(movie);

        //5.generate the posterUrl
        String posterUrl=baseUrl+"/file/"+uploadedFileName;

        //6.map Movie object to DTO object and return it
        MovieDto response=convertMovieToDto(savedMovie,posterUrl);

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //1.check the data in DB and if exists, fetch the data of given ID
        Movie movie =movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie not found with id: "+movieId));

        //2.generate posterUrl
        String posterUrl=baseUrl+"/file/"+movie.getPoster();

        //3.map to the MovieDto object and return it
        MovieDto response=convertMovieToDto(movie,posterUrl);

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //1.fetch all data from DB
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos=new ArrayList<>();

        //2.iterate through the list, generate posterUrl for each movie object
        //and map to MovieDto object
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+movie.getPoster();
            MovieDto response=convertMovieToDto(movie,posterUrl);
            movieDtos.add(response);
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {

        //1. check if movie object exists with given movieId
       Movie mv= movieRepository.findById(movieId)
               .orElseThrow(()->new MovieNotFoundException("Movie not found with id: "+movieId));

        //2.if file is null ,  do nothing
        // if file is not null, then delete existing file associated with the record,
        // and upload the new file

        String fileName=mv.getPoster();
        if(file!=null){
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName=fileService.uploadFile(path,file);
        }

        //3. set movieDto's poster value, according to step2
        movieDto.setPoster(fileName);

        //4. map it to Movie object
        Movie movie=new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );


        //5. save to movie object->return saved movie object
        Movie updatedMovie=movieRepository.save(movie);

        //6. generate posterUrl for it
        String posterUrl=baseUrl+"/file/"+fileName;

        //7. map to MovieDto and return it
        MovieDto response=convertMovieToDto(movie,posterUrl);

        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        //1. check if movie  object exists in DB
        Movie mv=movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie not found with id: "+movieId));
        Integer id=mv.getMovieId();

        //2. delete the file associated with this object
        Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));

        //3. delete the movie object
        movieRepository.delete(mv);

        return "Movie deleted  with id="+id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+movie.getPoster();
            MovieDto movieDto=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }

        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                                        moviePages.getTotalElements(),
                                        moviePages.getTotalPages(),
                                        moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort=dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                                                            :Sort.by(sortBy).descending();

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviesPages=movieRepository.findAll(pageable);

        List<Movie> movies=moviesPages.getContent();

        //convert movies to movieDto list
        List<MovieDto> movieDtos=new ArrayList<>();
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+movie.getPoster();
            MovieDto movieDto=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }

        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                                    moviesPages.getTotalElements(),
                                    moviesPages.getTotalPages(),
                                    moviesPages.isLast());
    }

    private MovieDto convertMovieToDto(Movie movie, String posterUrl){
         MovieDto movieDto=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return movieDto;
    };


}
