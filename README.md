# movie api 
### POST http://localhost:8080/api/v1/movie/add-movie
![image](https://github.com/user-attachments/assets/c7fae28b-1977-48b5-aca1-5f5630538145)

![image](https://github.com/user-attachments/assets/61258852-607c-4b5e-b3ad-7fa9fdae8641)

### GET http://localhost:8080/api/v1/movie/1

![image](https://github.com/user-attachments/assets/0f85c413-ffb8-4ce3-b2d0-de0e44e6395e)

### GET http://localhost:8080/api/v1/movie/all
![image](https://github.com/user-attachments/assets/81cdcfa9-d3d6-4d5c-b566-929551adf650)

### PUT  http://localhost:8080/api/v1/movie/update/1
![image](https://github.com/user-attachments/assets/a7375eb4-9dac-4c93-b21a-18544a102a04)

### DELETE http://localhost:8080/api/v1/movie/delete/2
![image](https://github.com/user-attachments/assets/04291a0f-ac2d-4de9-b0dc-1b546f35c0a5)

通過@ExceptionHandler統一處理 Exception 。  
它在背後利用了 AOP（面向切面編程，Aspect-Oriented Programming） 的理念，將異常處理邏輯從業務邏輯中分離開來，從而提高程式碼的可讀性和可維護性。
![image](https://github.com/user-attachments/assets/7b34b540-21b9-4860-9afb-32c7cc1bf3fa)

![image](https://github.com/user-attachments/assets/08783244-bbb2-4a3d-a812-bfe7051c68cd)

### GET http://localhost:8080/api/v1/movie/allMoviesPage  
分頁搜尋 pagination search
![image](https://github.com/user-attachments/assets/697f75c6-3736-4fa0-8900-ac7dc0d2c07a)

pageSize is number of data in single page
![image](https://github.com/user-attachments/assets/9893e24a-b2ae-4fdb-8f0e-50d7b99c3ca6)

### GET http://localhost:8080/api/v1/movie/allMoviesPage?pageNumber=1&pageSize=2 
![image](https://github.com/user-attachments/assets/a7aacc79-6718-41e1-8f36-cd88619bb3cd)

### GET http://localhost:8080/api/v1/movie/allMoviesPageSort?pageNumber=0&pageSize=6&sortBy=releaseYear&sortDir=asc  
用上映年分進行升序排列  
![image](https://github.com/user-attachments/assets/3dab5bc4-5965-4908-86c9-0ec38f1bf415)


### GET http://localhost:8080/api/v1/movie/allMoviesPageSort?pageNumber=1&pageSize=3&sortBy=movieId&sortDir=desc  
用movieId 進行降序排列  
![image](https://github.com/user-attachments/assets/32731908-13fa-4e1a-a2bb-ea74ce603544)


## JWT Authorization
![image](https://github.com/user-attachments/assets/00878187-0b2e-435d-87ff-11899dc3b04b)
Part 1  
->JWT dependency ->pom.xml  
->user model -> with 'refreshToken'  field  
->user repo   
->refreshToken -> model  
->refreshToken repo  
->ApplicationConfig-> for beans of important interfaces  / classes  
->JwtService  
->RefreshTokenService  
->AuthFilterService  

### POST http://localhost:8080/api/v1/auth/register  
![image](https://github.com/user-attachments/assets/b1f996f6-82fe-438c-93de-6199679f3f81)
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDFAZ21haWwuY29tIiwiaWF0IjoxNzMyODY5NzAzLCJleHAiOjE3MzI5NTYxMDN9.D7Qs55esAiJ9zHxh2Ca5HzDBZCl7rZ1q1ty3Q7unAw0",
    "refreshToken": "f1ac58b2-7469-487c-beb3-40c7d40b111d"
}  

![image](https://github.com/user-attachments/assets/892a6f57-3739-48e0-8472-825477196392)
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI4NzAwNDksImV4cCI6MTczMjk1NjQ0OX0.qDMaq4NnvDJZvNHUoJ7X_0DoFXhTze9E943XFtvNh1Q",
    "refreshToken": "d47764b5-1694-4474-9a74-e6fb56b74296"
}  

### GET http://localhost:8080/api/v1/movie/all  
![image](https://github.com/user-attachments/assets/52daebfc-ea0f-4d9b-8dc0-3097e1cecffc)

### POST http://localhost:8080/api/v1/auth/login  
![image](https://github.com/user-attachments/assets/bb7e8acd-0390-49b4-b30d-4ee28f4dfab3)
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDFAZ21haWwuY29tIiwiaWF0IjoxNzMyODcwNzQ1LCJleHAiOjE3MzI5NTcxNDV9.YRL_1e7GNhwTLYCI_RqGlJoQWVD53aNFsonc1d9neVY",
    "refreshToken": "f1ac58b2-7469-487c-beb3-40c7d40b111d"
}   


### POST http://localhost:8080/api/v1/auth/refresh  
換新 refresh token   
如果refresh token 已經到期的話  則必須重新登入獲取新的一組 (access token+ refresh token)  
![image](https://github.com/user-attachments/assets/70b70c08-1dd8-42f8-a103-252cde139b1e)  

### POST http://localhost:8080/api/v1/auth/login  
登入ADMIN 管理員  
![image](https://github.com/user-attachments/assets/7c568f59-f6ee-443c-a793-f6d692f865e7)  
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MzI4NzIwMDIsImV4cCI6MTczMjk1ODQwMn0.qKGOD-0tJT6aZ_zlXAHRHi-18AFnyoLO6sLRYXTKbns",
    "refreshToken": "d47764b5-1694-4474-9a74-e6fb56b74296"
}  

### POST http://localhost:8080/api/v1/movie/add-movie  
普通用戶嘗試上傳電影  
結果403 Forbidden 權限不足  
![image](https://github.com/user-attachments/assets/65f52716-b197-4334-a357-3037507ccb0b)

在Request Header中帶上管理員的access token 成功上傳電影  
![image](https://github.com/user-attachments/assets/58278db2-ed7b-46ef-9dd6-0e05189d2208)

### password  alter work flow  
![image](https://github.com/user-attachments/assets/e125c212-d87c-4f4c-8a6b-b03b149f64b0)
