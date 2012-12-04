(ns renic.todo
  (:require [goog.array :as goog-array]
            [crate.core :as crate])
  (:use-macros [crate.macros :only [defpartial defhtml]]))

;; for FireFox and opera hack
(extend-type js/DOMStringList
  ISeqable
  (-seq [array] (array-seq array 0)))

(def todo-personal-key "todo-personal")
(def todo-work-key "todo-work")
(def storage (.-localStorage js/window))

;; ========== htmls ==========
(defhtml icon-delete []
  [:div.icon-delete
   [:div.icon-delete-line-1]
   [:div.icon-delete-line-2]])

(defhtml through-line []
  [:div.through-line])

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


;; ========== delete ==========
(defn delete-li [button]
  (.log js/console "delete-li")
  (.removeChild (.. button -parentNode -parentNode) (.-parentNode button)))

(defn remove-saved [key todo-text]
  (.log js/console "remove")
  (.log js/console (str "key: " key))
  (.log js/console (str "todo-text: " todo-text))
  (let [saved-items (get-saved-items key)
        text-index (goog-array/indexOf saved-items todo-text)]
    (.splice saved-items text-index 1)
    (.setItem storage key (.stringify js/JSON saved-items))))

(defn on-delete-click [ev]
  (.log js/console "on-delete-click")
  (this-as button
           (remove-saved
            (.. ev -currentTarget -parentNode -parentNode -id)
            (.. button -parentNode -textContent))
           (delete-li button)))


;; ========== Click list ==========
(defn mark-done [li]
  (.log js/console "mark-done")
  (let [icon (icon-delete)
        line (through-line)]
    ;; (.addEventListener icon "click" on-delete-click false)
    (set! (.-onclick icon) on-delete-click)
    (.appendChild li line)
    (.appendChild li icon))
  (.setAttribute li "class" "item-done"))

(defn mark-ongoing [li]
  (.log js/console "mark-ongoing")
  (let [icon-delete (.getElementsByClassName li "icon-delete")
        line (.getElementsByClassName li "through-line")]
    (.removeChild li (.item line 0))
    (.removeChild li (.item icon-delete 0)))
  (.setAttribute li "class" "item-ongoing"))

(defn toggle-done [li]
  (.log js/console "toggle-done")
  (let [li-class (.getAttribute li "class")]
    (if (not= li-class "item-done")
      (mark-done li)
      (mark-ongoing li))))

(defn on-list-click []
  (.log js/console "on-list-click")
  (this-as li (toggle-done li)))


;; ========== add ==========
(defn li-on-dragstart [ev]
  (.log js/console "li-on-dragstart")
  (set! (.. ev -dataTransfer -effectAllowed) (array "copy" "move"))
  (.setData (.-dataTransfer ev) "text/plain" (.. ev -target -id)))

(defn add-li [key todo-text]
  (.log js/console "add-li")
  (let [li (.createElement js/document "li")
        ul (.getElementById js/document key)]
    (.setAttribute li "class" "item-ongoing")
    (.setAttribute li "id" todo-text)
    ;; (.addEventListener li "click" on-list-click false)
    (set! (.-draggable li) true)
    (set! (.-onclick li) on-list-click)
    (set! (.-innerHTML li) todo-text)
    (.addEventListener li "dragstart" li-on-dragstart false)
    (.appendChild ul li)))

(defn save [key todo-text]
  (.log js/console "save")
  (.log js/console (str "key: " key))
  (.log js/console (str "todo-text: " todo-text))
  (let [saved-items (get-saved-items key)]
    (.push saved-items todo-text)
    (.setItem storage key (.stringify js/JSON saved-items))))

(defn add-new-todo [todo-text]
  (.log js/console "add-new-todo")
  (add-li todo-personal-key todo-text)
  (save todo-personal-key todo-text))

(defn ^:export on-add-click []
  (.log js/console "on-add-click")
  (let [todo (.getElementById js/document "todo-input")
        todo-text (.-value todo)]
    (if (not= todo-text "") (add-new-todo todo-text) (js/alert "Please input a todo."))
    (set! (.-value todo) "")
    (.focus todo))
  false)


;; ========== drag and drop ==========
(defn ul-on-dragenter [ev]
  (.preventDefault ev))

(defn ul-on-dragover [ev]
  (.preventDefault ev)
  (set! (.. ev -dataTransfer -dropEffect) "copy"))

(defn ul-on-drop [ev]
  (.log js/console "ul-on-drop")
  (.preventDefault ev)
  (.stopPropagation ev)
  (if (> (.. ev -dataTransfer -types -length) 0)
    (doseq [type (.. ev -dataTransfer -types)]
      (.log js/console (str "types: " (.. ev -dataTransfer -types)))
      (.log js/console (str "type: " type))
      (if (= type "text/plain")
        (let [source-id (.getData (.-dataTransfer ev) "text/plain")
              source (.getElementById js/document source-id)
              source-perent (.-parentNode source)
              source-perent-id (.-id source-perent)
              target (.-currentTarget ev)
              target-id (.-id target)]
          (.appendChild target (.removeChild source-perent source))
          (save target-id source-id)
          (remove-saved source-perent-id source-id))))
    false))

(defn add-ul-listeners [ul]
  (.log js/console "add-ul-listeners")
  (doto ul
    (.addEventListener "dragenter" ul-on-dragenter false)
    (.addEventListener "dragover" ul-on-dragover false)
    (.addEventListener "drop" ul-on-drop false)))


;; ========== init ==========
(defn input-submit [ev]
  (.preventDefault ev)
  (on-add-click)
  false)

(defn input-key-down [ev]
  (.log js/console (str "keyCode: " (.-keyCode ev)))
  (if (=  13 (.-keyCode ev)) (input-submit ev) false))

(defn init-input []
  (let [input (.getElementById js/document "todo-input")]
    (doto input
      (.addEventListener "keydown" input-key-down false)
      ;; (.addEventListener "submit" input-submit false)
      (.focus))))

(defn init-uls []
  (add-ul-listeners (.getElementById js/document "todo-personal"))
  (add-ul-listeners (.getElementById js/document "todo-work")))

(defn load-todo-list [key]
  (.log js/console "load-todo-list")
  (.log js/console (str "key: " key))
  (let [saved-items (get-saved-items key)]
    (if (not= nil saved-items)
      (doseq [item saved-items]
        (add-li key item)))))

(defn ^:export init []
  (.log js/console "init")
  (init-input)
  (init-uls)
  (load-todo-list todo-personal-key)
  (load-todo-list todo-work-key))

;; (crate-page/include-css "/css/todo.css")

(set! (.-onload js/window) init)
