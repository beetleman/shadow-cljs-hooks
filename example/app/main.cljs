(ns app.main
  (:require [goog.object :as gobj]))

(defonce reset-count (atom 0))

(defn app [target-id]
  (let [el (js/document.getElementById target-id)]
    (gobj/set el "innerHTML"
              (str "reredner nr: "
                   (swap! reset-count inc)))))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))

(defn ^:dev/after-load start []
  (js/console.log "start")
  (app "app"))

(defn init []
  (start))
