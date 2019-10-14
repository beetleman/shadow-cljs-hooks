(ns shadow-cljs-hooks.fulcro-css
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [com.fulcrologic.fulcro-css.css-injection :as inj]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [shadow-cljs-hooks.css :as hooks.css]
            [shadow-cljs-hooks.symbols :as symbols]))

(defn generate
  ([component]
   (generate component {:pretty-print? false}))
  ([component garden-flags]
   (inj/compute-css {:component    (symbols/eval-symbol component)
                     :garden-flags garden-flags})))

(def output-file-key ::output-file)
(defn output-file [build-state]
  (get build-state output-file-key))

(s/def ::component symbol?)
(s/def ::output-dir ::hooks.spec/not-empty-string)
(s/def ::asset-path ::hooks.spec/not-empty-string)
(s/def ::options (s/keys :req-un [::output-dir ::asset-path ::component]
                         :opt-un [::hooks.css/garden-flags]))

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
    (hooks.css/assoc-path build-state (str asset-path "/" file-name))))

(defn hook
  {:shadow.build/stage :flush}
  ([build-state]
   (hook build-state {}))
  ([build-state options]
   (hook* build-state
          options
          {:write-css hooks.css/write!})))
