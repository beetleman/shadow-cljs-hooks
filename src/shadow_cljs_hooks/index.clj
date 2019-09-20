(ns shadow-cljs-hooks.index
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [hiccup.core :refer [html]]
            [shadow-cljs-hooks.spec :as hooks.spec]))

(defn read-edn [path]
  (clojure.edn/read-string (slurp path)))


(defn output-dir [build-state]
  (get-in build-state [:shadow.build/config :output-dir]))

(defn asset-path [build-state]
  (get-in build-state [:shadow.build/config :asset-path]))


(defn get-manifest [build-state]
  (-> (output-dir build-state)
      (str "/manifest.edn")
      read-edn))

(defn entry-point-js [{:keys [entry-point]}]
  (-> entry-point
      str
      (string/replace #"-" "_")
      (string/replace #"/" ".")
      (str "()")))

(defn template [build-state
                {:keys [title links scripts lang app-mount]
                 :as options}]
  (let [output-name (-> (get-manifest build-state)
                        first
                        :output-name)
        main-src (str (asset-path build-state)
                      "/"
                      output-name)
        js (entry-point-js options)]
    (html
     [:html {:lang lang}
      [:head
       [:title title]
       [:meta {:charset "utf-8"}]
       (for [href links]
         [:link {:rel "stylesheet"
                 :type "text/css"
                 :href href}])]
      [:body
       [app-mount "Loading..."]
       (for [src scripts]
         [:script {:src src}])
       [:script {:src main-src}]
       [:script js]]])))

(s/def ::path (s/and string? (complement empty?)))
(s/def ::lang (s/and string? (complement empty?)))
(s/def ::links (s/coll-of string?))
(s/def ::scripts (s/coll-of string?))
(s/def ::entry-point symbol?)
(s/def ::app-mount keyword?)
(s/def ::options (s/keys :opt-un [::path
                                  ::lang
                                  ::title
                                  ::scripts
                                  ::links]
                         :req-un [::entry-point]))

(defn conform-options [build-state options]
  (merge options {:path (string/replace (output-dir build-state)
                                        (re-pattern (asset-path build-state))
                                        "")
                  :title "ClojureScript 🥳🏆"
                  :links ["https://cdn.jsdelivr.net/npm/destyle.css@1.0.10/destyle.css"]
                  :scripts []
                  :lang "en"
                  :app-mount :div#app}))

(defn write-html [build-state options]
  {:pre [(hooks.spec/valid? ::hooks.spec/build-state build-state)
         (hooks.spec/valid? ::options options)]}
  (let [{:keys [path]
         :as options} (conform-options build-state options)
        index-html (template build-state options)]
    (spit (str path "/" "index.html")
          index-html)))
