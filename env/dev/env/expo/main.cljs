(ns ^:figwheel-no-load env.expo.main
  (:require [reagent.core :as r]
            [high-voltage.core :as core]
            [figwheel.client :as figwheel :include-macros true]
            [env.dev]))

(enable-console-print!)

(def counter (r/atom 0))
(defn reloader [] @counter [core/app-root])
(def root-el (r/as-element [reloader]))

(figwheel/watch-and-reload
 :websocket-url    (str "ws://" env.dev/ip ":3449/figwheel-ws")
 :heads-up-display true
 :jsload-callback  (fn []
                     (println "Reload" @counter)
                     (swap! counter inc)))
(core/init)
