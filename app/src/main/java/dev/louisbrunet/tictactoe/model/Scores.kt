package dev.louisbrunet.tictactoe.model

import android.os.Parcel
import android.os.Parcelable

data class Scores(
    val scoreX: Int = 0,
    val scoreO: Int = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(scoreX)
        parcel.writeInt(scoreO)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Scores> {
        override fun createFromParcel(parcel: Parcel): Scores {
            return Scores(parcel)
        }

        override fun newArray(size: Int): Array<Scores?> {
            return arrayOfNulls(size)
        }
    }
}