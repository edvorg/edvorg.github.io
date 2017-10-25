(ns edvorg.state
  (:require [edvorg.defaults :as defaults]
            [reagent.core :as reagent]))

(defonce state (reagent/atom defaults/state))
