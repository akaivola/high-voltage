(ns high-voltage.components
  (:require
   [reagent.core :as reagent :refer [atom]]
   [cuerdas.core :as str]
   [oops.core :as o :refer [oget]]))

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

(defn v [view-style & components]
  (into
    [view {:style (o/oget+ style (str/camel (name view-style)))}]
    components))

(defn camelize-keys [m]
  (clojure.walk/postwalk
    (fn [k]
      (if (keyword? k)
        (str/camel (name k))
        k))
    m))

(defn style-sheet [styles]
  (StyleSheet.create
    (clj->js
      (camelize-keys
        styles))))
