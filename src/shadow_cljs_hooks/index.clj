(ns shadow-cljs-hooks.index
  (:require [hiccup.core :refer [html]]))

(defn read-edn [path]
  (clojure.edn/read-string (slurp path)))

(defn get-root [build-state]
  (get-in build-state [:shadow.build/config :output-dir]))

(defn get-manifest [build-state]
  (read-edn (str (get-root build-state)
                 "/"
                 "manifest.edn")))

(defn template [manifest]
  (html
   [:html {:lang "en"}
    [:head
     [:meta {:charset "utf-8"}]]
    [:body
     [:div#app "loading..."]

     [:link
      {:href "https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
       :rel  "stylesheet"}]

     [:script {:src (str "js/" (-> manifest first :output-name))}]
     [:script "app.main.init();"]]]))

(defn write-html [build-state options]
  (let [manifest   (get-manifest build-state)
        index-html (template manifest)]
    (spit (:index-html-path options)
          index-html)))
