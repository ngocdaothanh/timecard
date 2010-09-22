function(doc) {
  if (doc['user_id'] != null && doc['date'] != null) {
    emit([doc.user_id, doc.date], 1);
  }
}
