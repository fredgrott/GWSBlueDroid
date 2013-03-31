/*
 * Copyright (C) 2012 Mobs and Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobsandgeeks.demo;

import gws.grottworkshop.gwsbluedroid.R;

import java.text.Collator;

import java.util.Arrays;


import com.mobsandgeeks.adapters.Sectionizer;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
* @author Ragunath Jawahar R <rj@mobsandgeeks.com>
* @version 0.1
*/
public class ArrayAdapterDemoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssa_list_view);

        // 1. Your data source
        String[] books = new String[] {
            "A Tale of Two Cities", "Le Petit Prince", "Dream of the Red Chamber", 
            "How to Win Friends and Influence People", "The Magic", 
            "And Then There Were None", "Think and Grow Rich", "She", "O Alquimista", 
            "Harry Potter and the Deathly Hallows", "Steps to Christ", "The Ginger Man",
            "Lolita", "Charlotte's Web", "Heidi's Years of Wandering and Learning", 
            "Anne of Green Gables", "Black Beauty", "Il Nome della Rosa", 
            "Watership Down", "The Secret", "Jonathan Livingston Seagull",
            "A Message to Garcia", "Sophie's World", "Angels and Demons",
            "How the Steel Was Tempered", "War and Peace", "You Can Heal Your Life",
            "Kane and Abel", "The Diary of Anne Frank", "To Kill a Mockingbird",
            "Valley of the Dolls", "Gone with the Wind", "One Hundred Years of Solitude",
            "Who Moved My Cheese?", "The Wind in the Willows", "Nineteen Eighty-Four",
            "Love Story", "Wolf Totem", "Jaws", "Love You Forever", 
            "What to Expect When You're Expecting", "Kon-Tiki: Across the Pacific in a Raft",
            "Where the Wild Things Are", "Fear of Flying", "Goodnight Moon", 
            "Guess How Much I Love You", "Perfume", "God's Little Acre", "Dune",
            "No Longer Human", "Catch-22", "Eye of the Needle", "Wild Swans"
        };

        // 2. Sort it
        Arrays.sort(books, 0, books.length, Collator.getInstance());

        // 3. Create your adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, books);

        // 4. Create a Sectionizer
        Sectionizer<String> alphabetSectionizer = new Sectionizer<String>() {

            @Override
            public String getSectionTitleForItem(String bookName) {
                return bookName.substring(0, 1);
            }
        };

        // 5. Wrap your adapter within the SimpleSectionAdapter
        SimpleSectionAdapter<String> sectionAdapter = new SimpleSectionAdapter<String>(this, 
                adapter, R.layout.ssa_section_header, R.id.title, alphabetSectionizer);

        // 6. Set the adapter to your ListView
        setListAdapter(sectionAdapter);
    }

}
