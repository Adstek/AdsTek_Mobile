package com.adstek.data.remote


data class ApiResponse<T>(
    val message: String,
    val data: T
)


//{
//    "message":{"password":["This field may not be blank."]},
//    "status_code":400
//}
//
//{
//    "message":{"email":["user with this email address already exists."]},
//    "status_code":400}
//
//{
//    "status":"success",
//    "message":"User created successfully",
//    "user_id":21
//}