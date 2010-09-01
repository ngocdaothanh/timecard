class Group
  include SimplyStored::Couch

  property :name

  validates_presence_of :name

  has_many :users
 
end
