(ns env.index
  (:require [env.dev :as dev]))

;; undo main.js goog preamble hack
(set! js/window.goog js/undefined)

(-> (js/require "figwheel-bridge")
    (.withModules #js {"./assets/fonts/OpenSans-ExtraBold.ttf" (js/require "../../../assets/fonts/OpenSans-ExtraBold.ttf"), "./assets/icons/loading.png" (js/require "../../../assets/icons/loading.png"), "./assets/fonts/OpenSans-BoldItalic.ttf" (js/require "../../../assets/fonts/OpenSans-BoldItalic.ttf"), "./assets/fonts/OpenSans-Bold.ttf" (js/require "../../../assets/fonts/OpenSans-Bold.ttf"), "expo" (js/require "expo"), "./assets/fonts/OpenSans-LightItalic.ttf" (js/require "../../../assets/fonts/OpenSans-LightItalic.ttf"), "./assets/images/cljs.png" (js/require "../../../assets/images/cljs.png"), "./assets/icons/app.png" (js/require "../../../assets/icons/app.png"), "react-native" (js/require "react-native"), "./assets/fonts/OpenSans-ExtraBoldItalic.ttf" (js/require "../../../assets/fonts/OpenSans-ExtraBoldItalic.ttf"), "react" (js/require "react"), "./assets/fonts/OpenSans-Italic.ttf" (js/require "../../../assets/fonts/OpenSans-Italic.ttf"), "./assets/fonts/LICENSE.txt" (js/require "../../../assets/fonts/LICENSE.txt"), "./assets/fonts/OpenSans-Light.ttf" (js/require "../../../assets/fonts/OpenSans-Light.ttf"), "./assets/fonts/OpenSans-SemiBold.ttf" (js/require "../../../assets/fonts/OpenSans-SemiBold.ttf"), "create-react-class" (js/require "create-react-class"), "./assets/fonts/OpenSans-SemiBoldItalic.ttf" (js/require "../../../assets/fonts/OpenSans-SemiBoldItalic.ttf"), "@expo/vector-icons" (js/require "@expo/vector-icons"), "./assets/fonts/OpenSans-Regular.ttf" (js/require "../../../assets/fonts/OpenSans-Regular.ttf")}
)
    (.start "main" "expo" "localhost"))
