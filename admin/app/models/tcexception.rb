class Tcexception
  include SimplyStored::Couch

  property :value

  validates_presence_of :value

end
