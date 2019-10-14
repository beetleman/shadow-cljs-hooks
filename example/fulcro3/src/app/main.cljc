(ns app.main
  (:require #?(:clj [com.fulcrologic.fulcro-css.localized-dom-server :as dom]
               :cljs [com.fulcrologic.fulcro-css.localized-dom :as dom])
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.mutations :as m]))

;; -- UI --

(defsc CountButton [this {::keys [counter]}]
  {:css           [[:.root {:background-color "#4CAF50"
                            :color            :white
                            :padding          "5px 20px"
                            :text-align       :center
                            :text-decoration  :none
                            :font-size        "20px"}]]
   :ident         (fn []
                    [::count-button :singleton])
   :query         [::counter]
   :initial-state {::counter 0}}
  (dom/button :.root
              {:onClick #(m/set-value! this ::counter (inc counter))}
              "click me (" counter ")"))


(def ui-count-button (comp/factory CountButton))

(defsc Root [this {:ui/keys [button]}]
  {:css [[:.root {:margin "30px"}]]
   :query [{:ui/button (comp/get-query CountButton)}]
   :initial-state (fn [_]
                    {:ui/button (comp/get-initial-state CountButton)})}
  (dom/div :.root
           (ui-count-button button)))

;; -- APP --

(defonce app (app/fulcro-app))

;; -- ENTRY POINT --

(defn ^:export init
  "Shadow-cljs sets this up to be our entry-point function. See shadow-cljs.edn `:init-fn` in the modules of the main build."
  []
  (app/mount! app Root "app")
  #?(:cljs (js/console.log "Loaded")))

(defn ^:export refresh
  "During development, shadow-cljs will call this on every hot reload of source. See shadow-cljs.edn"
  []
  ;; re-mounting will cause forced UI refresh, update internals, etc.
  (app/mount! app Root "app")
  #?(:cljs (js/console.log "Hot reload")))
