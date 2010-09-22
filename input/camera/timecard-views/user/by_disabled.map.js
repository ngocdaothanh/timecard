function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'User') {
             emit(doc['disabled'], 1);
           }
         }