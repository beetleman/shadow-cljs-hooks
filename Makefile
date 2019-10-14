.PHONY: test

test:
	clojure -A:dev -m kaocha.runner
_test_release_fulcro:
	cd ./example/fulcro3 && yarn shadow-cljs release app
_test_release_re-frame:
	cd ./example/re-frame && yarn shadow-cljs release app
test_release:_test_release_fulcro _test_release_re-frame
deploy:
	mvn deploy
