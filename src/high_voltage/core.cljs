(ns high-voltage.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [oops.core :refer [ocall]]
              [high-voltage.handlers]
              [high-voltage.subs]))

(def ReactNative (js/require "react-native"))
(def Expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))
(def Ionicons (.-Ionicons AtExpo))
(def ic (r/adapt-react-class ionicons))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))

(defn app-root []
  [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
   [image {:source (js/require "./assets/images/cljs.png")
           :style  {:width  200
                    :height 200
                    :font-family "open-sans-regular"}}]
   [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @(subscribe [:get-greeting])]
   [ic {:name "ios-arrow-down" :size 60 :color "green"}]
   [touchable-highlight {:style    {:background-color "#999" :padding 10 :border-radius 5}
                         :on-press #(alert "HELLO, biatchness")}
    [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])

(defn init []
  (dispatch-sync [:initialize-db])
  (ocall expo :registerRootComponent (r/reactify-component app-root))
  #_(-> (ocall expo :Font :loadAsync (clj->js
                                     {"open-sans-light"    "require('./assets/fonts/OpenSans-Light.ttf"
                                      "open-sans-regular"  "require('./assets/fonts/OpenSans-Regular.ttf"
                                      "open-sans-semibold" "require('./assets/fonts/OpenSans-SemiBold.ttf"
                                      "open-sans-bold"     "require('./assets/fonts/OpenSans-Bold.ttf"}))
      (ocall :then (fn []
                     (ocall expo :registerRootComponent (r/reactify-component app-root))))))
