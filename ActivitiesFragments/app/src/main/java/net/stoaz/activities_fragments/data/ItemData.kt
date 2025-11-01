package net.stoaz.activities_fragments.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ItemData(
    val name: String,
    val description: String = "No description provided."
) : Parcelable
