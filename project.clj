(defproject web-todo "0.1.0-SNAPSHOT"
  :description "Web base todo"
  :url ""
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir-cljs "0.3.0"]
                 [jayq "0.1.0-alpha1"]
                 [fetch "0.1.0-alpha2"]
                 [crate "0.1.0-alpha3"]
                 [noir "1.3.0-beta2"]]
  :plugins [[lein-cljsbuild "0.2.1"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [
               {:source-path "src/renic/todo/cljs",
                :compiler {;; :output-dir "resources/public/cljs/main",  ;; for debug mode
                           :output-to "js/todo.js",
                           :optimizations :simple,
                           ;; :optimizations :advanced,
                           :pretty-print true}}]}
  :main ^{:skip-aot true} web-todo.server)
