# Helper Utilities

This is a collection of helpful Data Binding and LiveData utilities.

  - CDSLiveData allows accessing the CDS Content Provider as LiveData objects
  - CDSProperty is an enum of all possible CDS properties
  - CDSValues adds some parsing support for some of the CDS values
  - DataBindingHelpers adds some helpful data binding attributes to some Views
    - A useful pattern is to return a Kotlin lambda that expects to run with a Context receiver, which is more ergonomic with these helpers
  - GsonNullable adds some extension methods to Gson to support nullable returns for unexpected object shapes
  - LiveDataHelpers adds some extension methods for LiveData objects to add extra compositions and transformations
