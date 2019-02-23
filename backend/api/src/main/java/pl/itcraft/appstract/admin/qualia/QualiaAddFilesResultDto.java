package pl.itcraft.appstract.admin.qualia;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaAddFilesResultDto {

	@GraphQLArgument(name = "input")
	private AddFilesResultDto addFiles;

	public AddFilesResultDto getAddFiles() {
		return addFiles;
	}

	public void setAddFiles(AddFilesResultDto addFiles) {
		this.addFiles = addFiles;
	}
	
	public class AddFilesResultDto {
		private AddFilesResultOrderDto order;
		
		public AddFilesResultOrderDto getOrder() {
			return order;
		}
		public void setOrder(AddFilesResultOrderDto order) {
			this.order = order;
		}

		public class AddFilesResultOrderDto {
			private PrimaryDocumentDto primary_document;
			
			public PrimaryDocumentDto getPrimary_document() {
				return primary_document;
			}
			public void setPrimary_document(PrimaryDocumentDto primary_document) {
				this.primary_document = primary_document;
			}
			
			public class PrimaryDocumentDto {
				private String _id;
				private String name;
				
				public String get_id() {
					return _id;
				}
				public void set_id(String _id) {
					this._id = _id;
				}
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
			}
		}
	}
	
}
