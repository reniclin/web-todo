(defproject web-todo "0.1.0-SNAPSHOT"
  :description "Web base todo"
  :url ""
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [jayq "0.2.0"]
                 [fetch "0.1.0-alpha2"]
                 [crate "0.1.0-alpha3"]
                 [noir "1.3.0-beta10"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [
               {:source-path "src/renic/todo/cljs",
                :compiler {;; :output-dir "resources/public/cljs/main",  ;; for debug mode
                           :output-to "js/todo.js",
                           :optimizations :simple,
                           ;; :optimizations :advanced,
                           :pretty-print true}}]}
  :main ^{:skip-aot true} web-todo.server)
