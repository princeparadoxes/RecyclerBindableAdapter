# RecyclerBindableAdapter

Abstract adapter for convenient work with RecyclerView.

## Overview

The library contains five adapters:
* RecyclerBindableAdapter - basic abstract adapter;
* FilterBindableAdapter - abstract adapter extends RecyclerBindableAdapter, allows you to filter the items in the list;
* ParallaxBindableAdapter - abstract adapter extends RecyclerBindableAdapter, allows you to apply to the Parallax header and footer;
* SimpleBindableAdapter and SimpleParallaxBindableAdapter - simple version of the adapter, have a basic set of functions. Allows you to create
adapter with a minimal amount of code.

## Installation

Add the following dependency to your gradle build file:

    dependencies {
        compile 'com.getkeepsafe.android.multistateanimation: library: 1.1.0'
    }

## Usage
### RecyclerBindableAdapter.
It allows more convenient to work with the data inside RecyclerView.
Basic methods:
* `Public void add (int position, T item)` - adds an item to the specified position;
* `Public void add (T item)` - adds an element to the end of the main list;
* `Public void addAll (List <? Extends T> items)` - adds a list of items to the end of the main list;
* `Public void set (int position, T item)` - replaces the element at the specified position;
* `Public void removeChild (int position)` - removes the element at the specified position;
* `Public void clear ()` - cleans the main list;
* `Public void moveChildTo (int fromPosition, int toPosition)` - moves the item to the specified position;
* `Public T getItem (int position)` - returns the element at the specified position;
* `Public int indexOf (T object)` - returns the position of the element in the list;
* `Public void addHeader (View header)` - adds a Header in RecyclerView;
* `Public void removeHeader (View header)` - removes Header from RecyclerView;
* `Public void addFooter (View footer)` - adds Footer in RecyclerView;
* `Public void removeFooter (View footer)` - removes the Footer of RecyclerView;
* `Public int getItemCount ()` - returns the number of elements in RecyclerView;
* `Public int getRealItemCount ()` - returns the number of elements in RecyclerView (without headers and footers).

To use RecyclerBindableAdapter need to extend them his adapter and create ViewHolder that extends RecyclerView.ViewHolder.
An example of the adapter:
```java
    // Expand our class from RecyclerBindableAdapter, as the parameters are        passed to a data type and ViewHolder
    public class LinearExampleAdapter extends RecyclerBindableAdapter<Integer, LinearViewHolder> {
    // Initialize ActionListener
    private LinearViewHolder.ActionListener actionListener;

    // Specify layoutId item
    @Override
    protected int layoutId(int type) {
        return R.layout.linear_example_item;
    }

    // Create a new instance of the view element
    @Override
    protected LinearViewHolder viewHolder(View view, int type) {
        return new LinearViewHolder(view);
    }

    // Pass data from the adapter element in ViewHolder
    @Override
    protected void onBindItemViewHolder(LinearViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position), position, actionListener);
    }

    // Setter for ActionListener
    public void setActionListener(LinearViewHolder.ActionListener actionListener) {
        this.actionListener = actionListener;
    }

}
```
### FilterBindableAdapter
Extends RecyclerBindableAdapter.
Less flexible and requires more memory, but allows you to filter elements.
The basic methods are not in RecyclerBindableAdapter:
* `Public Filter getFilter ()` - returns the filter;
* `Public void setOnFilterObjectCallback (OnFilterObjectCallback callback)` - set interface that allows you to keep track of the number of elements after filtration.

An example of using a filter:
```java
filterEditText.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

    // If you change the text inside filterEditText amended text is inserted as a filter
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterExampleAdapter.getFilter().filter(s);
            }

            Override
            public void afterTextChanged(Editable s) {

            }
        });
```
### ParallaxBindableAdapter
Extends RecyclerBindableAdapter, unlike him, can only have one Header and Footer.
The basic methods are not in RecyclerBindableAdapter:
* `Public void setParallaxHeader (boolean isParallaxHeader)` - set false if you do not need to apply for Parallax Header (default true);
* `Public void setParallaxFooter (boolean isParallaxFooter)` - set false if you do not need to apply for Parallax Footer (default true);
* `Public void setOnParallaxScroll (OnParallaxScroll parallaxScroll)` - listener to track parallaxScroll (for example, to hide the ActionBar).

### SimpleBindableAdapter and SimpleParallaxBindableAdapter
Extends RecyclerBindableAdapter and ParallaxBindableAdapter. Intended for simple lists.
They have a very simple initialization. We need to create a ViewHolder, be sure that enhances BindableViewHolder.
You can create your ActionListener which necessarily extends BindableViewHolder.ActionListener.
Example initialization:
```java
    // initialize SimpleBindableAdapter, indicates the type of items and ViewHolder inherited from BindableViewHolder
    private SimpleBindableAdapter<Integer, SimpleViewHolder> simpleExampleAdapter;

...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                    ...............
        // pass it layoutId item and class ViewHolder
        simpleExampleAdapter = new SimpleBindableAdapter<>(R.layout.simple_example_item, SimpleViewHolder.class);
        simpleExampleRecycler.setAdapter(simpleExampleAdapter);
                    ...............
    }

    @Override
    protected void onResume() {
        super.onResume ();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i <COUNT_ITEMS; i ++) {
            list.add (i + 1);
        }
        simpleExampleAdapter.clear();
        simpleExampleAdapter.addAll(list);
    }
```
## Sample application

See [sample application] (sample /) for an example.
Example [RecyclerBindableAdapter] (sample / src / main / javacom / danil / recyclerbindableadapter / sample / linear)
Example [FilterBindableAdapter] (sample / src / main / javacom / danil / recyclerbindableadapter / sample / filter)
Example [RecyclerBindableAdapter with two item types] (sample / src / main / javacom / danil / recyclerbindableadapter / sample / grid)
Example [SimpleBindableAdapter] (sample / src / main / javacom / danil / recyclerbindableadapter / sample / simple)
Example [SimpleParallaxBindableAdapter] (sample / src / main / javacom / danil / recyclerbindableadapter / sample / parallax)

## License

    Copyright 2015 Danil Perevalov.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.