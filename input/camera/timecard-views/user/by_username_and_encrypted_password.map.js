function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'User') {
             emit([doc['username'], doc['encrypted_password']], 1);
           }
         }