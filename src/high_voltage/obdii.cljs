(ns high-voltage.obdii
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as r]
   [oops.core :as o :refer [oget]]
   [cuerdas.core :as str]))

(def obd2 (js/require "react-native-obd2"))
