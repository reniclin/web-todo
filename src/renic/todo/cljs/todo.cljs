(ns renic.todo
  (:require [crate.core :as crate]
            [goog.array :as goog-array])
  (:use-macros [crate.macros :only [defpartial]]))


(def todo-storage-key "todolist")
(def storage (.-localStorage js/window))


;; ========== test ==========
(defn ^:export test-alert [li]
  (js/alert "test"))


;; ========== common ==========
(defn get-saved-items [key]
  (.log js/console "get-saved-items")
  (let [storage-item (.getItem storage key)]
    (if (or (= storage-item nil) (= storage-item ""))
      (array)
      (.parse js/JSON storage-item))))


;; ========== add ==========
(defn add-li [todo-text]
  (.log js/console "add-li")
  (let [li (.createElement js/document "li")
        ul (.getElementById js/document "todolist")]
    (.setAttribute li "onclick" "renic.todo.on_list_click(this)")
    (set! (.-innerHTML li) todo-text)
    (.appendChild ul li)))

(defn save [todo-text]
  (.log js/console "save")
  (let [saved-items (get-saved-items todo-storage-key)]
    (.push saved-items todo-text)
    (.setItem storage todo-storage-key (.stringify js/JSON saved-items))))

(defn add-new-todo [todo-text]
  (.log js/console "add-new-todo")
  (add-li todo-text)
  (save todo-text))

(defn ^:export on-add-click []
  (.log js/console "on-add-click")
  (let [todo-text (-> (.getElementById js/document "todoInput") (.-value))]
    (if (not= todo-text "") (add-new-todo todo-text) (js/alert "Please input a todo.")))
  (set! (-> (.getElementById js/document "todoInput") (.-value)) ""))


;; ========== delete ==========
(defn delete-li [button]
  (.log js/console "delete-li")
  (.removeChild (.. button -parentNode -parentNode) (.-parentNode button)))

(defn remove-saved [todo-text]
  (.log js/console "remove")
  (let [saved-items (get-saved-items todo-storage-key)
        text-index (goog-array/indexOf saved-items todo-text)] ;; this function does NOT work on Firefox
    (.splice saved-items text-index 1)
    (.setItem storage todo-storage-key (.stringify js/JSON saved-items))))

(defn ^:export on-delete-click [button]
  (.log js/console "on-delete-click")
  (remove-saved (.. button -parentNode -textContent))
  (delete-li button))


;; ========== Click list ==========
(defn mark-done [li]
  (.log js/console "mark-done")
  (let [button-delete (.createElement js/document "input")]
    (set! (.-type button-delete) "button")
    (set! (.-value button-delete) "X")
    (.setAttribute button-delete "class" "delete")
    (.setAttribute button-delete "onclick" "renic.todo.on_delete_click(this)")
    (.appendChild li button-delete))
  (.setAttribute li "class" "done"))

(defn mark-ongoing [li]
  (.log js/console "mark-ongoing")
  (let [button-delete (.getElementsByClassName li "delete")]
    (.removeChild li (.item button-delete 0)))
  (.setAttribute li "class" "ongoing"))

(defn toggle-done [li]
  (.log js/console "toggle-done")
  (let [li-class (.getAttribute li "class")]
    (if (not= li-class "done")
      (mark-done li)
      (mark-ongoing li))))

(defn ^:export on-list-click [li]
  (.log js/console "on-list-click")
  (toggle-done li))


;; ========== init ==========
(defn load-todo-list []
  (.log js/console "load-todo-list")
  (let [saved-items (get-saved-items todo-storage-key)]
    (if (not= nil saved-items)
      (doseq [item saved-items]
        (add-li item)))))

(defn ^:export init []
  (.log js/console "init")
  (.focus (.getElementById js/document "todoInput"))
  (load-todo-list))

(set! (.-onload js/window) init)
