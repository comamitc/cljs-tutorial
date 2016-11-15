(ns cljs-frontend.core
    (:require-macros [cljs.core.async.macros :refer [go go-loop]])
    (:require [rum.core :as rum]
              [cljs.core.async :refer [<! timeout]]
              [cljs-time.core :as t]
              [cljs-time.format :as f]))

(enable-console-print!)

;; -------------------------
;; State
(def state (atom {:count 0 :time ""}))

;; let's just print out our state
; (add-watch state :key (fn [_ _ old-state new-state] nil))

;; SKIP THIS FOR NOW, JUST A EMPTY COMPONENT FOR THE exercise
(rum/defc grouped-data []
  (let [*rand-data (rum/cursor state :rand-data)]
    ;; deref becuase the underlying datatype of a cursor is another atom
    (when @*rand-data
      [:div "placeholder"])))

;; A reactive component in rum can listen to a property (key) on a global state
;; object (map) and re-render a component (and only they component) when that
;; cursor changes
(rum/defc timer < rum/reactive []
  (let [*timer (rum/cursor state :time)]
    [:div.time-red (rum/react *timer)]))

(rum/defc home-page < rum/reactive []
  (let [*count (rum/cursor state :count)
        version (:version @state)]
    [:div
      [:h2 (str "Welcome to CLJS w/ Rum!" (when version (str " v" version)))]
      [:div.center-me
        [:button {:on-click #(swap! *count inc)} "Click Me!"]
        [:div (str "Clicks " (rum/react *count))]]
      (timer)
      (grouped-data)]))

;; -------------------------
;; Initialize app
(defn mount-root []
  (rum/mount (home-page) (.getElementById js/document "app")))

(defn ^:export init []
  (mount-root))

  ;; -------------------------
  ;; Misc Timer Stuff
(def time-format (f/formatter "MM/dd/yy HH:mm:ss"))
;; core.async creates a background go-channel and updates part of the state
;; every 100 milliseconds
(go-loop []
  (<! (timeout 100))
  (swap! state assoc :time (f/unparse time-format (t/now)))
  (recur))
