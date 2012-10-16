(ns renic.todo
  (:require [goog.array :as goog-array]
            [crate.core :as crate])
  (:use-macros [crate.macros :only [defpartial]]))


(def todo-storage-key "todolist")
(def storage (.-localStorage js/window))

;; ========== htmls ==========
(defpartial icon-delete []
  [:div.icon-delete
   [:div.icon-delete-line-1]
   [:div.icon-delete-line-2]])

(defpartial icon-delete-2 []
  [:div.icon-delete
   [:div.icon-delete-content "X"]])


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
        ul (.getElementById js/document "todo-list")]
    (.setAttribute li "class" "item-ongoing")
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
  (let [todo-text (-> (.getElementById js/document "todo-input") (.-value))]
    (if (not= todo-text "") (add-new-todo todo-text) (js/alert "Please input a todo.")))
  (set! (-> (.getElementById js/document "todo-input") (.-value)) ""))


;; ========== delete ==========
(defn delete-li [button]
  (.log js/console "delete-li")
  (.removeChild (.. button -parentNode -parentNode) (.-parentNode button)))

(defn remove-saved [todo-text]
  (.log js/console "remove")
  (let [saved-items (get-saved-items todo-storage-key)
        text-index (goog-array/indexOf saved-items todo-text)]
    (.splice saved-items text-index 1)
    (.setItem storage todo-storage-key (.stringify js/JSON saved-items))))

(defn ^:export on-delete-click [button]
  (.log js/console "on-delete-click")
  (remove-saved (.. button -parentNode -textContent))
  (delete-li button))


;; ========== Click list ==========
(defn mark-done [li]
  (.log js/console "mark-done")
  (let [icon (icon-delete)]
    (.setAttribute icon "onclick" "renic.todo.on_delete_click(this)")
    (.appendChild li icon))
  (.setAttribute li "class" "item-done"))

(defn mark-ongoing [li]
  (.log js/console "mark-ongoing")
  (let [icon-delete (.getElementsByClassName li "icon-delete")]
    (.removeChild li (.item icon-delete 0)))
  (.setAttribute li "class" "item-ongoing"))

(defn toggle-done [li]
  (.log js/console "toggle-done")
  (let [li-class (.getAttribute li "class")]
    (if (not= li-class "item-done")
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
  (.focus (.getElementById js/document "todo-input"))
  (load-todo-list))

(set! (.-onload js/window) init)
