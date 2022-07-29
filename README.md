[TOC]

# README

this project is a authenticate service based on HTTP

It provides APIs to 

- create/delete user/role
- bind role to user
- generate/invalidate token which will expire in a given time
- check the roles of a given token 

## Run
```
mvn clean package
```
```aidl
 java -jar .\target\JAuth-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## Test
use postman/curl to send Http request, e.g.,

```aidl
curl -XPOST http://localhost:9090/api/role -d '{
    "requestId": "dasrrtewty3543t432g34",
    "roleName": "role88"
}'
```
more requests can be found in each API document

## API

requests are in json format. All API use Http Post/Delete, **No GET Action**. input parameters are in HTTP Body,including a requestId and other input parameters. 

responses are in json format. 4 fixed fields: 
- requestId, 
- code
  - code=0 means success. otherwise failed.
- message
  - when code is not 0, the "message" will show the reason. e.g., User already exists
- retValue
  - the return value differs according to different APIs.
  - Most of the time, the return value is true/false indicates that create/delete/bind operation succeed or failed
  - In token generation API, the return value is the token; In Role check API, the return value is the list of Role that the user have.


### createRole

request sample
```aidl
POST /api/role 
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "roleName": "role1"
}
```

response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
    "retValue": true
}
```
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 1,
    "message": "Role role1 Exists",
    "retValue": false
}
```

### deleteRole
request sample
```aidl
DELETE /api/role 
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "roleName": "role1"
}
```

response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "code": "",
    "retValue": true
}
```
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 1,
    "message": "Role role1 Not Exists",
    "retValue": false
}
```

### createUser
request sample
```aidl
POST /api/user 
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "userName": "user1",
    "password": "123456"
}
```

response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
    "retValue": true
}
```
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 1,
    "message": "User user1 Exists",
    "retValue": false
}
```

### deleteUser
request sample
```aidl
DELETE /api/user 
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "userName": "user1"
}
```

response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "code": "",
    "retValue": true
}
```
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 1,
    "message": "User user1 Not Exists",
    "retValue": false
}
```


### getToken
request sample
```aidl
POST /api/auth
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "userName": "user1",
    "password": "123456"
}
```
response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
    "retValue": RcJ0_GGKo-ON00FTmQS22eHsvqFnXSe-
}
```

### invalidate
```aidl
DELETE /api/auth
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "token": " RcJ0_GGKo-ON00FTmQS22eHsvqFnXSe-"
}
```
response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
    "retValue": true
}
```

### checkRole
```aidl
POST /api/auth/role
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "token": " RcJ0_GGKo-ON00FTmQS22eHsvqFnXSe-",
    "roleName": "role1"
}
```
response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
    "retValue": true
}
```
token invalid/expired, do nothing

### getRoles
```aidl
POST /api/auth/roles
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "token": " RcJ0_GGKo-ON00FTmQS22eHsvqFnXSe-"
}
```
response sample
```aidl
{
    "requestId": "9ce04db2204f41e3a699831dd48e115c",
    "code": 0,
    "message": "",
//    "retValue": []
    "retValue": [
        {
            "name": "role1"
        },
        {
            "name": "role2"
        }
    ]
}
```

## Third-party Libraries
### slf4j and slf4j-simple
used for log print
### fastjson
used for request and response parse

## Design
Each path has a handler
```aidl
httpServer.createContext("/api/user", new UserHandler(userDao, roleDao));
httpServer.createContext("/api/role", new RoleHandler(roleDao, userDao));
httpServer.createContext("/api/auth", new AuthHandler(tokenDao));
```
these handler all extend the `BaseHandler`. In `BaseHandler.handle`, it called User/Role/AuthHandler's function `invoke`

In `invoke`, a DataAccessor(e,g., RoleDao) is used to access data by calling `roleDao.createRole`

RoleDao can have different Implementations, e.g., LocalRoleDao and  

in LocalRoleDao, a concurrent hash map is used to store User information. All data is in memory.

If we introduce mysql to store User information, just change RoleDao from LocalRoleDao to MysqlRoleDao 


