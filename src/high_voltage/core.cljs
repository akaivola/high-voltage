(ns high-voltage.core
    (:require [reagent.core :as reagent :refer [atom]]
              [re-frame.core :as r :refer [subscribe dispatch dispatch-sync]]
              [oops.core :as o :refer [oget]]
              [high-voltage.handlers]
              [cuerdas.core :as str]
              [high-voltage.subs]))

(def ReactNative (js/require "react-native"))
(def Expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))
(def Font (.-Font Expo))
(def Ionicons (.-Ionicons AtExpo))
(def ic (reagent/adapt-react-class Ionicons))

(def text (reagent/adapt-react-class (.-Text ReactNative)))
(def view (reagent/adapt-react-class (.-View ReactNative)))
(def image (reagent/adapt-react-class (.-Image ReactNative)))
(def StyleSheet (.-StyleSheet ReactNative))
(def touchable-highlight (reagent/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))

(defn camelize-keys [m]
  (clojure.walk/postwalk
    (fn [k]
      (if (keyword? k)
        (str/camel (name k))
        k))
    m))

(def style
  (StyleSheet.create
    (clj->js
      (camelize-keys
        {:gauges
         {:width          50
          :height         200}
         :gauge
         {:width            "100%"
          :height           100
          :flex-direction   "row"
          :background-color "green"}
         :gauge-positive
         {:width            "100%"
          :height           25
          :align-self       "flex-end"
          :background-color "rgb(0,255,0)"}
         :gauge-negative
         {:width            "100%"
          :height           10
          :align-self        "flex-start"
          :background-color "rgb(255,0,0)"}
         }))))

(defn v [view-style & components]
  (into
    [view {:style (o/oget+ style (str/camel (name view-style)))}]
    components))

(defn app []
  [view {:style {:margin 40 :align-items "center"}}
   #_[image {:source (js/require "./assets/images/cljs.png")
           :style  {:width  200
                    :height 200}}]
   #_[text {:style
          {:font-size     30
           :font-weight   "100"
           :margin-bottom 20
           :font-family   "open-sans-bold"
           :text-align    "center"}}
    @(subscribe [:get-greeting])]
   [view [text "foosbars"]]
   [v :gauges
    [v :gauge
     [v :gauge-positive]]
    [v :gauge
     [v :gauge-negative]]]
   #_[ic {:name "ios-arrow-down" :size 60 :color "green"}]
   [touchable-highlight {:style    {:background-color "#999" :padding 10 :border-radius 5}
                         :on-press #(alert "HELLO")}
    [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])

(defn app-root []
  (if-not @(r/subscribe [:query-db :init-complete?])
    [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
     [text
      {:style
       {:font-size     30
        :font-weight   "100"
        :margin-bottom 20
        :text-align    "center"}}
      "Loading"]]
    [app]))

(defn init []
  (dispatch-sync [:initialize-db])

  (-> (o/ocall Font :loadAsync
               (clj->js
                 {"open-sans-light"    (js/require "./assets/fonts/OpenSans-Light.ttf")
                  "open-sans-regular"  (js/require "./assets/fonts/OpenSans-Regular.ttf")
                  "open-sans-semibold" (js/require "./assets/fonts/OpenSans-SemiBold.ttf")
                  "open-sans-bold"     (js/require "./assets/fonts/OpenSans-Bold.ttf")}))
      (o/ocall :then
               (fn []
                 (println "Fonts loaded")
                 (r/dispatch [:set-db :init-complete? true]))))
  (o/ocall Expo :registerRootComponent (reagent/reactify-component app-root)))
