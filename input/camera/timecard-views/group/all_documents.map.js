function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'Group') {
             emit(doc['created_at'], 1);
           }
         }