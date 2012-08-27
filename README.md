web-todo
========

web-todo is a simple web base todo list app write in html / css / ClojureScript.

The todo data is store in localStorage.

the app is in very early stage, and no guarantee will keep development.

this app is for my own practice for html, css and ClojureScript.

### usage

1. use cljsc to compile cljs file into js file. (more infos about cljsc and ClojureScript [here](https://github.com/clojure/clojurescript/))

    cljsc todo.cljs '{:optimizations :simple :pretty-print true}' > todo.js

2. use browser to open the todo.html file, and try it.


### note

currently not work right with FireFox and IE. (maybe I'll fix it someday. lol)

work fine with Chrome, Opera and Safari.
