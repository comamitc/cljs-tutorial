# ClojureScript Bootcamp

> Take the knowledge you learned from the [CLJ(S) Intro](https://github.com/comamitc/clj-s-intro) and apply it in this exercise!

## Instructions

### Some Setup

- Install Java 8+ (I use `brew update && brew cask install java`)

- Install Leiningen (`brew install leiningen`)

*YAY! YOU HAVE A CLOJURE BUILD TOOL!*

#### Pull this repo locally if you haven't already.

### Let's Write Some ClojureScript

**note:** I've purposely already set up some of the tooling for you with this project.  Tooling is an ever expanding and changing landscape so rather than focus on those bits we should focus on the language.

1. You can run the app at any time with: `$> lein dev`. This:
    * Cleans existing build artifacts

    * Compiles the `less->css`

    * Compiles the CLJS

    * Starts a Figwheel Server for "Hot Reloading" @ <http://localhost:3449>


2. Look at `src/cljs_frontend/core.cljs` and you'll see some similar concepts and some new ones.
    * `ns` (namespace) is declared at the top of the file.

    * `:require` statements import internal & external dependencies via their namespaces.

    * I've left comments for the rest of the sections to get an idea of what's going on when you visit <http://localhost:3449>.

    * It's important to note we are using [Rum](https://github.com/tonsky/rum): _A Simple React Wrapper_. This keeps us from having to introduce (and learn) something like opinionated state management.

    * Also, we're using [`core.async`](https://github.com/clojure/core.async) that adds support for asynchronous programming using channels.  We are going to build upon this library in our exercise.


3. Writing Some ClojureScript
    * **state changes**: Errrbody loves them some timetraveling, right?  Let's utilize the [add-watch](http://clojuredocs.org/clojure.core/add-watch) function available for ClojureScript `atom`s to print out the old and new state changes when they the state is actually different! **hint**: The [`indentical?`](http://clojuredocs.org/clojure.core/identical%3F) function can be used here. You're add watch will be set up like this:

    ```Clojure
    (add-watch state
               :key
               (fn [_ _ old-state new-state]
                 ;; your code here
                 )
    ```

    * Next, let's create a macro that reads `./project.clj` and attached the version number to the `state`.  [Here's an example of this]( https://github.com/HigherEducation/edudirect-ai/blob/master/src/edudirect_ai/util.clj#L35-L36) **BUT** you will also want to convert the string produced from the input contents (via `slurp`) to a ClojureScript datastructure (via `read-string`).

    * **Asyncronous** OK, we're done with the easy stuff.  Lets go full out. For this exercise we will be querying a [random number generator](http://qrng.anu.edu.au/API/api-demo.php#) that generates true random numbers by measuring quantum fluctuations of a vacuum in real-time! Once we've gathered the result back we will reduce the returned array of data into a map (key-value pairs) with the key being a returned number in the JSON payload and the value being the number of times it occurred. For readability purpose, sort this map by it's values descending.

        * We're putting these series of functions outside of a `defn`. This can be in `core.cljs` or you can put it in your own file. (**hint**: Don't forget to import your new namespace into `core.cljs`!)

        * Use this URL for our JSON call: <https://qrng.anu.edu.au/API/jsonI.php?length=1000&type=uint8>

        * In order to get the data, use JavaScript's `fetch` api.

        * We can turn a `thenable` into a async channel with something like this:

        ```clojure
        (defn query-rand []
            (let [c (chan)]
              (.then (promise "some-args")
                     (fn [result]
                       ;; put the result on the channel and close it
                        (put! c result #(close!)))
              ;; ultimately return the channel
              c)))
        ```

        * Once the channel has resolved the data, we can pluck the data from that channel within a go block. And add the computed grouped and sorted data to the state object for rendering (on a key called `:rand-data`)

        * Don't forget to keep: <http://cljs.info/cheatsheet/> handy for looking up the functions that you can accomplish the grouping and sorting with.
