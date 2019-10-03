(ns shadow-cljs-hooks.fulcro-css
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [com.fulcrologic.fulcro-css.css-injection :as inj]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [shadow-cljs-hooks.css :as css]
            [taoensso.timbre :as log]))

(defn generate
  ([component]
   (generate component {:pretty-print? false}))
  ([component garden-flags]
   (require (symbol (namespace component)) :reload)
   (inj/compute-css {:component (eval component)
                     :garden-flags garden-flags})))

(def output-file-key ::output-file)
(defn output-file [build-state]
  (get build-state output-file-key))

(s/def ::component symbol?)
(s/def ::output-dir ::hooks.spec/not-empty-string)
(s/def ::asset-path ::hooks.spec/not-empty-string)

;;TODO: spec garden
;;TODO: PR to garden
(s/def :garden/pretty-print? boolean?)
(s/def :garden/output-to nil?) ; protect user form set it
(s/def ::garden-flags (s/keys :opt-un [:garden/pretty-print? :garden/output-to]))
(s/def ::options (s/keys :req-un [::output-dir ::asset-path ::component]
                         :opt-un [::garden-flags]))

(defn write-css! [css output-dir file-name]
  (when-not (.exists output-dir)
    (.mkdirs output-dir))
  (try
    (spit (str output-dir "/" file-name) css)
    (log/info "css compiled")
    (catch Exception e
      (log/error "css compilation failed" e))))


(defn hook* [build-state
             options
             {:keys [write-css]}]
  {:pre [(hooks.spec/valid? ::hooks.spec/build-state build-state)
         (hooks.spec/valid? ::options options)]}
  (let [{:keys [output-dir asset-path component]} options
        output-dir                                (io/file output-dir)
        file-name                                 "main.css"]
    (write-css (generate component)
               output-dir
               file-name)
    (css/assoc-path build-state (str asset-path "/" file-name))))

(defn hook
  {:shadow.build/stage :flush}
  ([build-state]
   (hook build-state {}))
  ([build-state options]
   (hook* build-state
          options
          {:write-css write-css!})))
