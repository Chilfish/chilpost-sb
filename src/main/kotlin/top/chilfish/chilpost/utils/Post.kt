package top.chilfish.chilpost.utils

import top.chilfish.chilpost.CROP_LENGTH


fun String.cropText(length: Int = CROP_LENGTH) =
    if (this.length > length)
        this.substring(0, length) + "..."
    else this