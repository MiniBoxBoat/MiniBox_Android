package com.jay86.minibox.config

/**
 * api列表
 * Created by jay68 on 2017/11/20.
 */

const val BASE_URL = "http://box.jay86.com:8080/minibox/"
const val QR_KEY = "6A1E5E1D364A51C9"
const val LOGIN = "user/login.do"
const val REGISTER = "user/register.do"
const val UPDATE_USER_INFO = "user/updateUserInfo.do"
const val UPDATE_AVATAR = "user/updateAvatar.do"
const val RESET_PASSWORD = "user/updatePassword.do"
const val SEND_SMS = "user/sendSms.do"
const val SEARCH_BY_POINT = "group/showBoxGroupAround.do"
const val SHOW_BOX_GROUP = "group/showBoxGroup.do"
const val APPOINT = "reservation/reserve.do"
const val ORDER = "order/order.do"
const val SHOW_USING_BOX = "box/showUserUsingBoxes.do"
const val SHOW_APPOINTING_BOX = "box/showUserReservingBoxes.do"

const val BASE_UPLOAD_URL = "http://gz.file.myqcloud.com/"
const val UPLOAD_FILE = "files/v2/1253275459/images/{filename}"

const val BASE_TOKEN_URL = "http://api.jay86.com/"
const val GET_TOKEN = "tecent/getToken.php"