(ns high-voltage.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :as r :refer [subscribe dispatch dispatch-sync]]
   [oops.core :as o :refer [oget]]
   [high-voltage.handlers]
   [high-voltage.obdii :as obdii]
   [cuerdas.core :as str]
   [high-voltage.subs]))

(def ReactNative (js/require "react-native"))
(def Expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))

(def Font (oget Expo "Font"))
(def Svg (oget Expo "Svg"))
(def Ionicons (oget AtExpo "Ionicons"))
(def StyleSheet (oget ReactNative "StyleSheet"))
(def Alert (oget ReactNative "Alert"))

(def ic (reagent/adapt-react-class Ionicons))
(def image (reagent/adapt-react-class (oget ReactNative "Image")))
(def text (reagent/adapt-react-class (oget ReactNative "Text")))
(def touchable-highlight (reagent/adapt-react-class (oget ReactNative "TouchableHighlight")))
(def view (reagent/adapt-react-class (oget ReactNative "View")))

(def svg (reagent/adapt-react-class Svg))
(def circle (reagent/adapt-react-class (oget Svg "Circle")))
(def rect (reagent/adapt-react-class (oget Svg "Rect")))
(def line (reagent/adapt-react-class (oget Svg "Line")))

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
        {}))))

(defn v [view-style & components]
  (into
    [view {:style (o/oget+ style (str/camel (name view-style)))}]
    components))

(defn power-bar []
  (let [kilo-watt 0]
    [view {:style
           {:justify-content "center"
            :align-items     "center"}}
     [text {:style
            {:font-family   "open-sans-bold"
             :font-size     15
             :margin-bottom 5}}
      "Power"]
     [svg {:height 200
           :width  70}
      [rect
       {:x      10
        :y      0
        :width  50
        :height 150
        :stroke "green"
        :fill   "white"}]

      (if (pos? kilo-watt)
        [rect
         {:x      10
          :y      (- 100 kilo-watt)
          :width  50
          :height kilo-watt
          :fill   "red"}]
        [rect
         {:x      10
          :y      100
          :width  50
          :height (* -1 kilo-watt)
          :fill   "green"}])

      [line
       {:x1           0
        :y1           100
        :x2           70
        :y2           100
        :stroke       "black"
        :stroke-width 2}]]]))

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

   [power-bar]

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
