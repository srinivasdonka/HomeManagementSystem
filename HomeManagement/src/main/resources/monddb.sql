db.autoinc.insert(
   {
      _id: "id",
      seq: 0
   }
);
 
// function to add sequence
function getNextSequence(name) {
   var ret = db.autoinc.findAndModify(
          {
            query: { _id: name },
            update: { $inc: { seq: 1 } },
            new: true
          }
   );
 
   return ret.seq;
}
 
 
For user table  we can create that with below 2 methods:
 
 
db.createCollection("user" , {
                              validator: { bsonType: "object",
                              required: ["id", "maker_timestamp"],
                              properties: {
                              id: { bsonType: "number"},
                              username: {
                                                            bsonType : "string"},
                              password: {
                                                bsonType: "string"},
                              first_name: {
                                                            bsonType : "string"},
                              designation: {
                                                            bsonType : "string"},
                              last_name: {
                                                            bsonType : "string"},
                              address: {
                                                            bsonType : "string"},
                              company: {
                                                            bsonType : "string"},
                              phone: {
                                                            bsonType : "number"},
                              maker_timestamp: new Date()}}})
 
 
db.createCollection("oauth_refresh_token" , {
                              validator: { bsonType: "object",
                              properties: {
                              token_id: { bsonType: "string"},
                              token: {
                                                            bsonType : "string"},
                              authentication: {
                                                bsonType: "string"},
                              }}})
 
 
db.createCollection("oauth_client_details" , {
                              validator: { bsonType: "object",
                              required: ["client_id"],
                              properties: {
                              client_id: { bsonType: "string"},
                              resource_ids: {
                                                            bsonType : "string"},
                              client_secret: {
                                                bsonType: "string"},
                              scope: {
                                                            bsonType : "string"},
                              authorized_grant_types: {
                                                            bsonType : "string"},
                              web_server_redirect_uri: {
                                                            bsonType : "string"},
                              authorities: {
                                                            bsonType : "string"},
                              access_token_validity: {
                                                            bsonType : "number"},
                              refresh_token_validity: {
                                                            bsonType : "number"},
                              additional_information: {
                                                            bsonType : "string"},
                              autoapprove: { bsonType: "string"}}}})
 
                             
Note: In order to create the primary in Mongo db is not possible but we can create the unique index or we can implement auto increment.
db.oauth_client_details.createIndex( { "client_id": 1 }, { unique: true } )
 
 
 
db.createCollection("oauth_approvals" , {
                              validator: { bsonType: "object",
                              required: ["expiresAt", "lastModifiedAt"],
                              properties: {
                              userId: { bsonType: "string"},
                              clientId: {
                                                            bsonType : "string"},
                              scope: {
                                                bsonType: "string"},
                              status: {
                                                            bsonType : "string"},
                              expiresAt: new Date(),
                              lastModifiedAt: new Date()}}})
 
 
 
 
db.createCollection("oauth_access_token" , {
                              validator: { bsonType: "object",
                              required: ["authentication_id"],
                              properties: {
                              token_id: { bsonType: "string"},
                              token: {
                                                            bsonType : "string"},
                              authentication_id: {
                                                bsonType: "string"},
                              user_name: {
                                                            bsonType : "string"},
                              client_id: {
                                                            bsonType : "string"},
                              authentication: {
                                                            bsonType : "string"},
                              refresh_token: {
                                                            bsonType : "string"}}}})
                                                            
db.createCollection("company_master" , {
                              validator: { bsonType: "object",
                              required: ["id", "name", "created_date"],
                              properties: {
                              id: { bsonType: "number"},
                              name: { bsonType : "string"},
                              address: {bsonType: "string"},
                              created_date: new Date(),
                              is_active: {bsonType : "number"},
                              is_SI: { bsonType : "number"},
                              last_upated: new Date(),
                              no_of_users: {bsonType : "number"}}}})
                                                                                                                                                                                                                                
 
db.createCollection("role" , {
                              validator: { bsonType: "object",
                              required: ["id"],
                              properties: {
                              id: { bsonType: "number"},
                              name: {bsonType : "string"},
                              description: {bsonType: "string"}}}})
                              
 
db.role.createIndex( { "name": 1 }, { unique: true } )
 
db.createCollection("privilege_config" , {
                              validator: { bsonType: "object",
                              required: ["id", "privilege_id", "role_id"],
                              properties: {
                              id: { bsonType: "number"},
                              prop-name: {bsonType : "string"},
                              role_id: {bsonType: "number"},
                              prop_value: { bsonType: "string"}}}})
                                                                                                                                                                                   
 
 
 