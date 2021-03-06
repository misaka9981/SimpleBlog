(function(tinymce) {
  tinymce.PluginManager.add('fontspacing', function(editor, url, $) {
    editor.on('init', function() {
      editor.formatter.register({
        fontspacing: { inline: 'span', styles: { 'letter-spacing': '%value' }}
      })
    })

    editor.addButton('fontspacingselect', function() {
      var items = [], defaultLineHeightFormats = '8pt 10pt 12pt 14pt 18pt 24pt 36pt'
      var fontspacing_formats = editor.settings.fontspacing_formats || defaultLineHeightFormats
      fontspacing_formats.split(' ').forEach(function(item) {
        var text = item, value = item
                 var values = item.split('=')
        if (values.length > 1) {
          text = values[0]
          value = values[1]
        }
        items.push({ text: text, value: value })
      })
      return {
        type: 'listbox',
        text: 'Font Spacing',
        tooltip: 'Font Spacing',
        values: items,
        fixedWidth: true,
        onPostRender: function() {
          var self = this
          editor.on('nodeChange', function(e) {
            var formatName = 'fontspacing'
            var formatter = editor.formatter
            var value = null
            e.parents.forEach(function(node) {
              items.forEach(function(item) {
                if (formatName) {
                  if (formatter.matchNode(node, formatName, { value: item.value })) {
                    value = item.value
                  }
                } else {
                  if (formatter.matchNode(node, item.value)) {
                    value = item.value
                  }
                }
                if (value) {
                  return false
                }
              })
              if (value) {
                return false
              }
            })
            self.value(value)
          })
        },
        onselect: function(e) {
          tinymce.activeEditor.formatter.apply('fontspacing', { value: this.value() })
        }
      }
    })
  })

  tinymce.PluginManager.requireLangPack('fontspacing', 'de')
})(tinymce)

