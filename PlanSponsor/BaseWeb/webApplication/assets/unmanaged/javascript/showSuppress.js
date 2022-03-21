  // Hides, disables and resets the specified element and all child elements by id
  function hideResetAndDisableNodesById(id) {
    hideResetAndDisableNodes(document.getElementById(id));
  }

  // Hides, disables and resets the specified element and disables and resets all child elements
  function hideResetAndDisableNodes(node) {

    hideNode(node);
    resetAndDisableNodes(node);
  }

  // Disables and resets the specified element and all child elements
  function resetAndDisableNodes(node) {

    disableNode(node);
    resetNode(node);

    if (node.childNodes && node.childNodes.length > 0) {
      for (var i = 0; i < node.childNodes.length; i++) {
        // Filter out non-element nodes
        if (node.childNodes[i].nodeType == 1) {
          resetAndDisableNodes(node.childNodes[i]);
        }
      }
    }
  }

  // Hides and disables the specified element and all child elements by id
  function hideAndDisableNodesById(id) {
    hideAndDisableNodes(document.getElementById(id));
  }

  // Hides and disables the specified element and disables all child elements
  function hideAndDisableNodes(node) {

    hideNode(node);
    disableNodes(node);
  }

  // Disables the specified element and all child elements
  function disableNodes(node) {

    disableNode(node);

    if (node.childNodes && node.childNodes.length > 0) {
      for (var i = 0; i < node.childNodes.length; i++) {
        // Filter out non-element nodes
        if (node.childNodes[i].nodeType == 1) {
          disableNodes(node.childNodes[i]);
        }
      }
    }
  }

  // Disables the specified element
  function disableNode(node) {

    // Disable element
    try {
      node.disabled = true;
    }
    catch(E){}
  }

  // Hides the specified element
  function hideNode(node) {
    try {
      node.style.display='none';
    }
    catch(E){}
  }

  // Hides the specified element by id
  function hideNodeById(id) {
    hideNode(document.getElementById(id));
  }

  // Hides and resets the specified element and all child elements by id
  function hideAndResetNodesById(id) {
    hideAndResetNodes(document.getElementById(id));
  }

  // Hides and resets the specified element and disables all child elements
  function hideAndResetNodes(node) {

    hideNode(node);
    resetNodes(node);
  }

  // Resets the specified element and all child elements
  function resetNodes(node) {

    resetNode(node);

    if (node.childNodes && node.childNodes.length > 0) {
      for (var i = 0; i < node.childNodes.length; i++) {
        // Filter out non-element nodes
        if (node.childNodes[i].nodeType == 1) {
          resetNodes(node.childNodes[i]);
        }
      }
    }
  }

  // Resets the specified element
  function resetNode(node) {

    // Clear element if text or select
    if (node.nodeName.toLowerCase() == 'input') {
      if (node.type == 'text' || node.type == 'hidden') {
        node.value = '';
      } else if ((node.type == 'radio') || node.type == 'checkbox') {
        node.checked = false;
      }
    } else if (node.nodeName.toLowerCase() == 'select') {
      node.selectedIndex = 0;
    }
  }

  // Shows and enables the specified element and all child elements by id
  function showAndEnableNodesById(id) {
    showAndEnableNodes(document.getElementById(id));
  }

  // Shows and enables the specified element and all child elements
  function showAndEnableNodes(node) {

    showNode(node);
    enableNodes(node);
  }

  // Enables the specified element and all child elements
  function enableNodes(node) {

    enableNode(node);

    if (node.childNodes && node.childNodes.length > 0) {
      for (var i = 0; i < node.childNodes.length; i++) {
        // Filter out non-element nodes
        if (node.childNodes[i].nodeType == 1) {
          enableNodes(node.childNodes[i]);
        }
      }
    }
  }

  // Enables the specified element
  function enableNode(node) {

    // Enable element
    try {
      node.disabled = false;
    }
    catch(E){}
  }

  // Shows the specified element (assumes we are dealing with block elements)
  function showNode(node) {
    try {
      node.style.display='block';
    }
    catch(E){}
  }

  // Shows the specified element by id
  function showNodeById(id) {
    showNode(document.getElementById(id));
  }
