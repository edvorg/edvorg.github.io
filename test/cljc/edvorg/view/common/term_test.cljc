(ns edvorg.view.common.term-test
  (:require [edvorg.view.common.term :as term]
            [clojure.test :as t]
            [clojure.string :as s]))

(t/deftest term-test
  (t/is (= (term/split-by-words "khun,,ph_uan")
            ["khun" "ph_uan"]))

  (t/is (= (term/split-by-consonants "ph_uan")
            ["p" "h" "_ua" "n"]))

  (t/is (= (term/get-tone-vowels \o)
            #{\a \e \i \o \u}))

  (t/is (= (term/shift-tone-left "_ooó")
            "_óoo"))

  (t/is (= (term/make-consonant-partitioned-words "r_oóy,phöm,nängs_üuphim")
            [["r" "_óo" "y"]
            ["p" "h" "ŏ" "m"]
            ["n" "ă" "n" "g" "s" "_ŭu" "p" "h" "i" "m"]]))

  (t/is (= (->> (term/make-consonant-partitioned-words "pen,phöm,pai,b_oorísàt,khon,phák,châangthàayrûup,krungthêep,nängs_üuphim,roongphayaabaan,klây,yâak,b_ò_y,hïw,ngâay,leekhäa,n_ùa_y,yuù,khun,kin,thîi,nákthúrákìt,thiî,angkrìt,tham,b_òy,n_uay,rák,r_óon,klâi,r_oóy,nám,b_ùa,mày,thamngaan,àrai,khäw,wítsawák_oon,raw,näaw,n_óoy,naamsakun,nängs_üu,süay,dii,duu,khonkhäayd_òokmái,d_ùum,d_òokmái,f_aan,kaaf_áa,b_àapf_ùkhàt,ìm,faràngsèet,phûujàtkaan,roongedvorg,mâak,mii,jaidii,jangwàt,maa,námsôm,rúujàk,yîipùn,khrua,kanbâan,häa,leekhäanúkaan,khonkhäayphönlamái,lên,kh_öothôot,ph_aang,khruu,châangtàts_ûa,m_öo,châangthamphöm,phûuchaay,ch_ûu,khonkhàprót,rót,bâan,n_oon,mâi,mài,")
                (term/join-words)
                (s/join ","))
           "pen,phŏm,pai,b_oorísàt,khon,phák,châangthàayrûup,krungthêep,năngs_ŭuphim,roongphayaabaan,klây,yâak,b_ò_y,hĭw,ngâay,leekhăa,n_ùa_y,yùu,khun,kin,thîi,nákthúrákìt,thîi,angkrìt,tham,b_òy,n_uay,rák,r_óon,klâi,r_óoy,nám,b_ùa,mày,thamngaan,àrai,khăw,wítsawák_oon,raw,năaw,n_óoy,naamsakun,năngs_ŭu,sŭay,dii,duu,khonkhăayd_òokmái,d_ùum,d_òokmái,f_aan,kaaf_áa,b_àapf_ùkhàt,ìm,faràngsèet,phûujàtkaan,roongedvorg,mâak,mii,jaidii,jangwàt,maa,námsôm,rúujàk,yîipùn,khrua,kanbâan,hăa,leekhăanúkaan,khonkhăayphŏnlamái,lên,kh_ŏothôot,ph_aang,khruu,châangtàts_ûa,m_ŏo,châangthamphŏm,phûuchaay,ch_ûu,khonkhàprót,rót,bâan,n_oon,mâi,mài")))
