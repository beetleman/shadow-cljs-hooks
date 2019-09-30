(ns shadow-cljs-hooks.fulcro-css
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [com.fulcrologic.fulcro-css.css-injection :as inj]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [shadow-cljs-hooks.css :as css]
            [taoensso.timbre :as log]))

(defn generate [component]
  (require (symbol (namespace component)) :reload)
  (inj/compute-css {:component (eval component)}))

(def output-file-key ::output-file)
(defn output-file [build-state]
  (get build-state output-file-key))

(s/def ::component symbol?)
(s/def ::output-dir (s/and string? (complement empty?)))
(s/def ::asset-path (s/and string? (complement empty?)))
(s/def ::options (s/keys :req-un [::output-dir ::asset-path ::component]))

(defn write-css [build-state options]
  {:pre [(hooks.spec/valid? ::hooks.spec/build-state build-state)
         (hooks.spec/valid? ::options options)]}
  (let [{:keys [output-dir asset-path component]} options
        output-dir  (io/file output-dir)
        output-file (str output-dir "/main.css")]
    (when-not (.exists output-dir)
      (.mkdirs output-dir))
    (try
      (spit output-file (generate component))
      (log/info "css compiled")
      (catch Exception e
        (log/error "css compilation failed" e)))

    (css/assoc-path build-state (str asset-path "/main.css"))))

(defn hook
  {:shadow.build/stage :flush}
  ([build-state]
   (hook build-state {}))
  ([build-state options]
   (write-css build-state options)))
