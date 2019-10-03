(ns shadow-cljs-hooks.index
  (:require [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [hiccup.core :refer [html]]
            [shadow-cljs-hooks.css :as css]
            [shadow-cljs-hooks.lang :as lang]
            [shadow-cljs-hooks.spec :as hooks.spec]))

(defn read-edn! [path]
  (edn/read-string (slurp path)))

(defn output-dir [build-state]
  (get-in build-state [:shadow.build/config :output-dir]))

(defn asset-path [build-state]
  (get-in build-state [:shadow.build/config :asset-path]))

(defn get-manifest-path [build-state]
  (str (output-dir build-state) "/manifest.edn"))

(defn get-manifest! [build-state]
  (read-edn! get-manifest-path))

(def entry-point-js-error-message
  "console.debug('shadow-cljs-hooks.index: ', 'no `:entry-point` provided.')")

(defn entry-point-js [{:keys [entry-point]}]
  (if entry-point
    (-> entry-point
        str
        (string/replace #"-" "_")
        (string/replace #"/" ".")
        (str "();"))
    entry-point-js-error-message))

(defn template [main-src
                {:keys [title links scripts lang app-mount]
                 :as options}]
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
     [:script (entry-point-js options)]]]))

(s/def ::path string?)
(s/def ::links (s/coll-of ::hooks.spec/not-empty-string))
(s/def ::scripts (s/coll-of ::hooks.spec/not-empty-string))
(s/def ::title ::hooks.spec/not-empty-string)
(s/def ::entry-point symbol?)
(s/def ::app-mount keyword?)
(s/def ::lang ::lang/codes)
(s/def ::options (s/keys :opt-un [::path
                                  ::lang
                                  ::title
                                  ::scripts
                                  ::entry-point
                                  ::links]))

(defn conform-options [build-state options]
  (merge {:path      (string/replace (output-dir build-state)
                                     (re-pattern (asset-path build-state))
                                     "")
          :title     "ClojureScript 🥳🏆"
          :links     ["https://cdn.jsdelivr.net/npm/destyle.css@1.0.10/destyle.css"]
          :scripts   []
          :lang      "en"
          :app-mount :div#app}
         options))

(defn add-css-link [build-state options]
  (if-let [link (css/get-path build-state)]
    (update options :links conj link)
    options))

(defn write-index-html! [path index-html]
  (spit (str path "/" "index.html")
        index-html))

(defn write-html! [build-state options]
  {:pre [(hooks.spec/valid? ::hooks.spec/build-state build-state)
         (hooks.spec/valid? ::options options)]}
  (let [{:keys [path]
         :as options} (->> (conform-options build-state options)
                           (add-css-link build-state))
        main-src (str (asset-path build-state)
                      "/"
                      (-> (get-manifest! build-state)
                          first
                          :output-name))
        index-html (template main-src options)]
    (write-index-html! path index-html)
    build-state))


(defn hook
  {:shadow.build/stage :flush}
  ([build-state]
   (hook build-state {}))
  ([build-state options]
   (write-html! build-state options)))
