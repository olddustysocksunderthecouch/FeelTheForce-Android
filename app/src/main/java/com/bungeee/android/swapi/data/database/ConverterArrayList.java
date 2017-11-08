///*
// * Copyright (C) 2017 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */

package com.bungeee.android.swapi.data.database;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * {@link TypeConverter} for long to {@link Date}
 * <p>
 * This stores the date as a long in the database, but returns it as a {@link Date}
 */
class ConverterArrayList {
    @TypeConverter
    public static ArrayList toArrayList(String string) {

        return new ArrayList<String>(Arrays.asList(string.split(" , ")));
    }

    @TypeConverter
    public static String toStingFromList(ArrayList arrayList) {
		return android.text.TextUtils.join(" , ", arrayList);

    }

    
}