package com.robert.mymovies.utils

class Resource<T>(val status: Status, val data: T?, val message: String?) {
    enum class Status { SUCCESS, ERROR, LOADING }
}