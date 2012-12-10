web-todo
========

web-todo is a simple web base todo list app write in html / css / ClojureScript.

Inspired by [any.DO](http://www.any.do/).

The todo data is store in localStorage.

the app is in very early stage, and no guarantee will keep development.

this app is for my own practice for html, css and ClojureScript.

### usage

1. compile cljs to js (more infos about cljsc and ClojureScript [here](https://github.com/clojure/clojurescript/))

    use cljsc to compile cljs file into js file.

   ```$ cljsc todo.cljs '{:optimizations :simple :pretty-print true}' > todo.js```

    or use [lein-cljsbuild](https://github.com/emezeske/lein-cljsbuild) command

    ```$ lein cljsbuild auto```

2. compile scss to css (you should know about [sass](http://sass-lang.com/) and [compass](http://compass-style.org/) first.)

    ```$ compass watch```

3. use browser to open the todo.html file, and try it.

### samples

If you know nothing about ClojureScript and sass/scss/compass, you can just use your browser open samples/todo.html

it included all needed js and css code, no need to complie, just try.

### note

currently not work right with IE. (maybe I'll fix it someday. lol)

work fine with Chrome, FireFox, Opera and Safari.

### resources
[Clojure](http://clojure.org/)
[ClojureScript](https://github.com/clojure/clojurescript)
[sass](http://sass-lang.com/)
[Compass](http://compass-style.org/)
[fancy-buttons](https://github.com/imathis/fancy-buttons)
[Animate Mixin for Compass/SASS](http://thecssguru.freeiz.com/animate/)