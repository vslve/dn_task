HOW TO RUN

1. Create database
2. Create table 'users'. Table descripion:

            id     | integer               | not null | 

            name   | character varying(25) | not null | 

 
 3. Create table 'records'. Table description:

             id           | integer               | not null |
             phone_owner  | character varying(25) | not null | 
             phone_number | character varying(25) | not null | 
             user_id      | integer               | not null | 

             FOREIGN KEY (user_id) REFERENCES users(id)
 
 4. Go to userphonebooks/src/main/resources
 5. Place created database name instead 'dbname' in application.properties and set other data base settings
 6. Go to userphonebooks/
 7. Execute ./gradlew build command 
 8. Execute ./gradlew bootRun command



ALLOWED METHODS

Users:

GET /users - to get all users

GET /users?name={name} - to get user by name or partname

response body format: 

                                    [
                                        {   
                                            "id" : id
                                            "name" : "{name}"
                                            "records : [
                                                {
                                                    "id" : id,
                                                    "userId" : userId
                                                    "phone_owner" : "phone_owner",
                                                    "phone_number" : "phone_number"
                                                }
                                            ]
                                        }
                                    ]

GET /users/{id} - to get user with id {id}

DELETE /user/{id} - to delete user with id {id}

response body format:
                
                            {   
                                "id" : {id}
                                "name" : "name"
                                "records : [
                                    {
                                        "id" : id,
                                        "userId" : userId
                                        "phone_owner" : "phone_owner",
                                        "phone_number" : "phone_number"
                                    }
                                ]
                            }



PUT /users/{id} - to edit user with id {id}

POST /users - to add new user

request body format: 

                             {"name":"name"}, 

                             5 <= name length <= 15 

response body format:
                
                            {   
                                "id" : {id}
                                "name" : "name"
                                "records : [
                                    {
                                        "id" : id,
                                        "userId" : userId
                                        "phone_owner" : "phone_owner",
                                        "phone_number" : "phone_number"
                                    }
                                ]
                            }
                            

Records:

GET /users/{id}/records - to get all records for user with id {id}

GET /users/{id}/records?phone={phone} - to get records whith phone_number full matched {phone} for user whith id {id}, 
                                        {phone} - sequence of digits, length = 11


response body format: 
            
                        [
                            {
                                "id" : recordId,
                                "userId" : {id}
                                "phone_owner" : "phone_owner",
                                "phone_number" : "phone_number"
                            }

                        ]
           
GET /users/{id}/records/{recordId} - to get record with id {recordId} for user with id {id}

DELETE /users/{id}/records/{recordId} - to delete record with id {recordId} for user with id {id}

response body format: 
           
                            {
                                "id" : {recordId},
                                "userId" : {id}
                                "phone_owner" : "phone_owner",
                                "phone_number" : "phone_number"
                            }


PUT /users/{id}/records/{recordId} - to edit record with id {recordId} for user with id {id}

POST /users/{id}/records - to add new record for user with id {id}

request body format: 
                    
                        {
                            "phoneOwner" : "phone_owner",
                            "phoneNumber" : "phone_number"
                        }, 

                        5 <= phone_owner length <= 15
                        phone_number - sequence of digits, length = 11
                        
response body format: 
           
                            {
                                "id" : recordId,
                                "userId" : {id}
                                "phone_owner" : "phone_owner",
                                "phone_number" : "phone_number"
                            } 

