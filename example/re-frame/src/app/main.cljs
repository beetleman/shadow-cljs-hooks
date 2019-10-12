(ns app.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [app.css :as css]))


;; -- EVENTS --
(rf/reg-event-db ::initialize
                 (constantly {::counter 0}))


(rf/reg-event-db ::inc-counter
                 (fn [db _]
                   (update db ::counter inc)))


;; -- SUBS --

(rf/reg-sub
  ::counter
  (fn [db _]
    (::counter db)))


;; -- UI --

(defn count-button
  []
  (let [counter @(rf/subscribe [::counter])]
    [:button
     {:on-click #(rf/dispatch [::inc-counter])
      :class    [css/count-button]}
     "click me (" counter ")"]))

(defn root
  []
  [:div {:class [css/root]}
   [count-button]])


;; -- ENTRY POINT --

(defn render
  []
  (reagent/render [root]
                  (js/document.getElementById "app")))

(defn clear-cache-and-render!
  []
  (rf/clear-subscription-cache!)
  (render))

(defn init
  []
  (rf/dispatch-sync [::initialize])
  (render))
