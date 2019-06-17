import TextEditor from "./textEditor";
import TextViewer from "./textViewer";

export function chooseViewer(viewer, value) {

  if(!viewer){
    return TextViewer;
  }
  return viewer;
}

export function chooseEditor(editor, value) {
  if(!editor){
    return TextEditor;
  }
  return editor;
}

