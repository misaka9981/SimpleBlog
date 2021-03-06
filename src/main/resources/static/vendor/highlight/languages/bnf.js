 

function(hljs){
  return {
    contains: [
             {
        className: 'attribute',
        begin: /</, end: />/
      },
             {
        begin: /::=/,
        starts: {
          end: /$/,
          contains: [
            {
              begin: /</, end: />/
            },
                         hljs.C_LINE_COMMENT_MODE,
            hljs.C_BLOCK_COMMENT_MODE,
            hljs.APOS_STRING_MODE,
            hljs.QUOTE_STRING_MODE
          ]
        }
      }
    ]
  };
}
