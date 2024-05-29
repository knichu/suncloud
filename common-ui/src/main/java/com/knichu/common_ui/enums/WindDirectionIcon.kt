package com.knichu.common_ui.enums

enum class WindDirectionIcon(val value: String, val icon: Int) {
    NORTH_WIND("0", com.knichu.common_ui.R.drawable.ic_north),
    NORTH_EAST_WIND("1", com.knichu.common_ui.R.drawable.ic_north_east),
    EAST_WIND("2", com.knichu.common_ui.R.drawable.ic_east),
    SOUTH_EAST_WIND("3", com.knichu.common_ui.R.drawable.ic_south_east),
    SOUTH_WIND("4", com.knichu.common_ui.R.drawable.ic_south),
    SOUTH_WEST_WIND("5", com.knichu.common_ui.R.drawable.ic_south_west),
    WEST_WIND("6", com.knichu.common_ui.R.drawable.ic_west),
    NORTH_WEST_WIND("7", com.knichu.common_ui.R.drawable.ic_north_west),
    NORTH_WIND2("8", com.knichu.common_ui.R.drawable.ic_north),
    NONE("", com.knichu.common_ui.R.drawable.ic_question_24dp);

    companion object {
        fun getIconImage(value: String?): WindDirectionIcon {
            return values().firstOrNull { it.value == value } ?: NONE
        }
    }
}