[![CircleCI](https://circleci.com/gh/beetleman/shadow-cljs-hooks.svg?style=svg)](https://circleci.com/gh/beetleman/shadow-cljs-hooks)
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.beetleman/shadow-cljs-hooks.svg)](https://clojars.org/org.clojars.beetleman/shadow-cljs-hooks)

# shadow-cljs-hooks

This library provides collections of useful hooks for [shadow-cljs]:

- `shadow-cljs-hooks.index` - generating `index.html`
- `shadow-cljs-hooks.fulcro-css` - generating CSS file for [Fulcro3]

## Getting Started

Add the following dependency to your `deps.edn` file:

``` clojure
org.clojars.beetleman/shadow-cljs-hooks {:mvn/version "current version from clojars"}
```

Add some [build hook] to `shadow-cljs.edn`, eg. `shadow-cljs-hooks.index/hook`:

``` clojure
{...
 :builds
 {:app {:target ...
        :build-hooks
        [(shadow-cljs-hooks.index/hook)]
        ...}}}}
```

checkout [fulcro3 example] to seeing it in action.

## Hooks

### `shadow-cljs-hooks.index`

Generate `index.html`  based on provided options, works with `:module-hash-names true`.

#### options

- `:path` - (optional, default: generated from `:output-dir` and `:asset-path`) where `index.html`
should be located
- `:lang` - (optional, default: `"en"`') `<html lang=...>`
- `:title` - (optional, default: `"ClojureScript 🥳🏆"`) `<title>...</title>`
- `:scripts` - (optional, default: `[]`) list of included `.js` files
- `:entry-point` - (optional, default: `nil`) entry point for application, eg.: `app/init`.
Optional because `:init-fn` in [modules configuration] can handle it
- `:links` - (optional, default: `[]`) list of included `.css` files

#### example

``` clojure
[(shadow-cljs-hooks.index/hook {:links   ["https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css"]
                               :scripts ["https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"]})]
```

### `shadow-cljs-hooks.fulcro-css`

Generate `CSS` files from [fulcro3] project. Works with `index` hook.

#### requirements

- [garden]
- [fulcro3]

#### options

- `:output-dir` (required) the directory to use for [garden] output
- `:asset-path` (required) the relative path from web server’s root to the resources
in `:output-dir`
- `:component` (required) `Root` component from which `CSS` will be generated
- `:garden-flags` (optional, default `{:pretty-print? false}`) [garden] flags

#### example

``` clojure
[(shadow-cljs-hooks.fulcro-css/hook {:component  app.main/Root
                                     :output-dir "public/css"
                                     :asset-path "/css"})
 (shadow-cljs-hooks.index/hook)]
```

## TODO

- [ ] pure garden hook
- [ ] reagent example
- [ ] code cleanup

[shadow-cljs]: https://github.com/thheller/shadow-cljs
[fulcro3]: https://github.com/fulcrologic/fulcro
[garden]: https://github.com/noprompt/garden
[build hook]: https://shadow-cljs.github.io/docs/UsersGuide.html#build-hooks
[fulcro3 example]: ./example
[modules configuration]: https://shadow-cljs.github.io/docs/UsersGuide.html#_modules
