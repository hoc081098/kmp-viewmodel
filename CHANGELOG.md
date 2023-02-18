# Change Log

## [Unreleased]

### Changed

- Make `ViewModel.viewModelScope` public.

### Added

- Add an `ViewModel.addCloseable` API and a new constructor overload `constructor(vararg closeables: Closeable)`,
  that allow you to add one or more `Closeable` objects to the `ViewModel`
  that will be closed when the `ViewModel` is cleared without requiring any manual work in `onCleared()`.

## [0.0.1] - Feb 11, 2023

- Initial release.

[Unreleased]: https://github.com/hoc081098/FlowExt/compare/0.0.1...HEAD
[0.0.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.0.1
